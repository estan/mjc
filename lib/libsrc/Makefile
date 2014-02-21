PREFIX=/info/primp05
INSTALL=/usr/sbin/install

all: libminijava.a runtime.s

libminijava.a: runtime.o
	ar -r -c libminijava.a runtime.o

runtime.s: runtime.c
	cc -S -O runtime.c
	mv runtime.s runtime-opt.s
	cc -S runtime.c

runtime.o: runtime.c
	cc -c -O runtime.c

install:
	$(INSTALL) -d -m 755 $(PREFIX)/libsrc
	$(INSTALL) -f $(PREFIX)/libsrc -m 644 Makefile
	$(INSTALL) -f $(PREFIX)/libsrc -m 644 libminijava.a
	$(INSTALL) -f $(PREFIX)/libsrc -m 644 runtime.c

clean:
	rm -f libminijava.a runtime.o runtime.s runtime-opt.s *~
