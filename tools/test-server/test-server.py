#!/usr/bin/env python

"""
Very simply test server for mjc.

The server handles POST requests to /. Each request must contain a multipart
file attachment called "file". The file should contain assembly code (.s).
The server will link the assembly code with libminijava using GCC and run the
resulting executable.

The server responds with either

    { "status": "success", "output": "<program output>" }

if all went well, or

    { "status": "error", "output": "<linker/program output>" }

if linking failed or the resulting executable left a non-zero status code.
"""

from tempfile import mkdtemp
from shutil import rmtree
from subprocess import check_output, CalledProcessError, STDOUT
from bottle import route, request, run

@route('/', method='POST')
def handle_file():
    try:
        # Create temporary working directory
        workdir = mkdtemp()

        # Copy supplied file to workdir
        infile = request.files['file'];
        infile.save(workdir)

        # Set up infile and outfile paths
        infile = workdir + '/' + infile.filename
        outfile = workdir + '/a.out'

        # Link with libminijava to produce outfile
        check_output(
            ['gcc', '-o', outfile, '-L', 'libsrc', infile, '-lminijava'],
            stderr=STDOUT
        )

        # Execute outfile
        output = check_output(outfile, stderr=STDOUT)

        # Success!
        return {'result': 'success', 'output': output}

    except CalledProcessError as e:
        # Error: Process had non-zero exit status
        return {'result': 'error', 'returncode': e.returncode, 'cmd': e.cmd, 'output': e.output}

    finally:
        # Clean up
        rmtree(workdir)

run(host='0.0.0.0', port=8080)
