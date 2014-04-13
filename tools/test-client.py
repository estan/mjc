#!/usr/bin/env python3

"""
Basic test client script.

The script requires Python Requests which can be installed with
`pip install --user requests`.

Usage: test-client.py java|mjc|compare <infile> [<classname>]

Subcommands:
    java     Compiles and runs <infile> using Java, printing the output.
    mjc      Compiles and runs <infile> using mjc, printing the output.
    compare  Runs the above two subcommands and compares the result.
             If the result is the same "OK" is printed, otherwise "FAIL"
             is printed followed by the output from the two subcommands.

The "mjc" subcommand links and executes the compiler output on a test
server which must be running at the URL specified by the MJC_TEST_URL
environment variable.

The optional parameter "classname" specifies which class to run when
invoking the JVM. If it is not specified it is assumed to be the name
of the input file without the suffix.

Examples:

    $ ./test-client.py java Foo.java
    42
    $ ./test-client.py mjc Foo.java
    42
    $ ./test-client.py compare Foo.java
    OK

    $ ./test-client.py java Bar.java
    43
    $ ./test-client.py mjc Bar.java
    42
    $ ./test-client.py compare Bar.java
    FAIL
    Java output:
    43
    mjc output:
    42
"""

import sys
from os import getenv
from os.path import basename,splitext,join,realpath,dirname
from shutil import copy
from tempfile import TemporaryDirectory
from requests import post
from subprocess import check_output,CalledProcessError,STDOUT

MJC_TEST_URL = getenv('MJC_TEST_URL')
MJC_SCRIPT_PATH = join(dirname(dirname(realpath(__file__))), 'mjc')

def run_java(infile, classname = None):
    with TemporaryDirectory() as workdir:
        copy(infile, workdir)
        base = basename(infile)
        infile = join(workdir, base)
        classname = splitext(base)[0] if classname == None else classname
        check_output(['javac', infile], stderr=STDOUT)
        return check_output(['java', '-cp', workdir, classname], stderr=STDOUT)

def run_mjc(infile):
    with TemporaryDirectory() as workdir:
        copy(infile, workdir)
        base = basename(infile)
        infile = join(workdir, base)
        check_output([MJC_SCRIPT_PATH, infile], stderr=STDOUT)
        asmfile = splitext(infile)[0] + '.s'
        result = post(MJC_TEST_URL, files={'file': open(asmfile, 'rb')}).json()
        if result['result'] == 'error':
            # "Simulate" the exception occurring locally.
            raise CalledProcessError(
                result['returncode'], result['cmd'],
                result['output'].encode(sys.getdefaultencoding()))
        return result['output'].encode(sys.getdefaultencoding())

def print_usage():
    print('Usage: test-client.py java|mjc|compare <infile> [<classname>]')

def print_decoded(b):
    print(b.decode(sys.getdefaultencoding()), end='')

if __name__ == '__main__':
    if len(sys.argv) not in [3, 4]:
        print_usage()
        sys.exit(1)

    cmd = sys.argv[1]
    infile = sys.argv[2]
    classname = sys.argv[3] if len(sys.argv) == 4 else None

    if cmd in ['java', 'compare'] and MJC_TEST_URL == None:
        print('MJC_TEST_URL not set!', file=sys.stderr)
        sys.exit(1)

    try:
        if cmd == 'java':
            print_decoded(run_java(infile, classname))
        elif cmd == 'mjc':
            print_decoded(run_mjc(infile))
        elif cmd == 'compare':
            try:
                java_out = run_java(infile, classname)
                mjc_out = run_mjc(infile)
                if mjc_out == java_out:
                    print('OK')
                else:
                    print('FAIL')
                    print('Java output:')
                    print_decoded(java_out)
                    print('mjc output:')
                    print_decoded(mjc_out)
            except CalledProcessError as e:
                print('FAIL')
                raise
        else:
            print_usage()
            sys.exit(1)
    except CalledProcessError as e:
        print_decoded(e.output)
