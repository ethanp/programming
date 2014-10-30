SYS_UNAME = $(shell uname)
ifeq ($(SYS_UNAME), Linux)
CXX = g++-4.8
else
CXX = g++
endif
LINK_TIME=$(shell date)
GIT_SHA=$(shell git rev-parse HEAD)
CFLAGS = -Wall -g -O2 -std=gnu++11 -Igetoptpp
ifeq ($(SYS_UNAME), Linux)
DBG_CFLAGS = -Wall -g -fsanitize=address -fno-omit-frame-pointer -std=gnu++11 -Igetoptpp
else
DBG_CFLAGS = -Wall -g -fno-omit-frame-pointer -std=gnu++11 -Igetoptpp
endif

libsrc := node.cpp paxserver.cpp paxos_exec.cpp paxos_vc.cpp paxclient.cpp \
paxlog.cpp paxmsg.cpp word_vec_pax.cpp log.cpp net.cpp dssim.cpp \
paxos_print.cpp args.cpp md5.cpp

#Files that are only necessary in C executable
exesrc := main.cpp

libdep := $(patsubst %.cpp,%.d,$(libsrc))
exedep := $(patsubst %.cpp,%.d,$(exesrc))

libobj := $(patsubst %.cpp,%.o,$(libsrc))
exeobj := $(patsubst %.cpp,%.o,$(exesrc)) getopt_pp.o

# debug
libdobj := $(patsubst %.cpp,%.og,$(libsrc))
exedobj += $(patsubst %.cpp,%.og,$(exesrc)) getopt_pp.og

# pull in dependency info
-include $(libdep)
-include $(exedep)

getopt_pp.o: getoptpp/src/getopt_pp.cpp getoptpp/getoptpp/getopt_pp.h
	$(CXX) -c $(CFLAGS) getoptpp/src/getopt_pp.cpp -o getopt_pp.o
getopt_pp.og: getoptpp/src/getopt_pp.cpp getoptpp/getoptpp/getopt_pp.h
	$(CXX) -c $(DBG_CFLAGS) getoptpp/src/getopt_pp.cpp -o getopt_pp.og

pax: $(libdep) $(exedep) $(libobj) $(exeobj)
	$(CXX) -o pax $(exeobj) $(libobj) -DGIT_SHA="\"$(GIT_SHA)\"" -DLINK_TIME="\"$(LINK_TIME)\"" version.cpp

pax.dbg: $(libdep) $(exedep) $(libdobj) $(exedobj)
ifeq ($(SYS_UNAME), Linux)
	$(CXX) -o pax.dbg $(exedobj) $(libdobj) -DGIT_SHA="\"$(GIT_SHA)\"" -DLINK_TIME="\"$(LINK_TIME)\"" version.cpp -lasan
else
	$(CXX) -o pax.dbg $(exedobj) $(libdobj) -DGIT_SHA="\"$(GIT_SHA)\"" -DLINK_TIME="\"$(LINK_TIME)\"" version.cpp
endif

#dependences
%.d:%.cpp
	$(CXX) -MM $(CFLAGS) $*.cpp > $*.d

# amd64 release
%.o: %.cpp
	$(CXX) -c $(CFLAGS) $*.cpp -o $*.o
#amd64 debug, evidently .go is a Go file.
%.og: %.cpp
	$(CXX) -c $(DBG_CFLAGS) $*.cpp -o $*.og
# preprocess
%.i: %.cpp
	$(CXX) -E $(CFLAGS) $*.cpp -o $*.i

t:
	$(CXX) $(CFLAGS) -o t t.cpp

.PHONY: valgrind
valgrind:
	valgrind --tool=memcheck --leak-check=full -v --show-reachable=yes --track-origins=yes ./pax

.PHONY: perf-record
perf-record:
	perf record -f -g ./pax -c 15 -r 7500
.PHONY: perf-report
perf-report:
	perf report -g

.PHONY: test
test: pax
# Basic functionality
	pax -c 1 -r 10 -l WARN
	pax -c 1 -r 10 -l WARN --seed 101
# Even number of servers
	pax -s 4 -c 1 -r 10 -l WARN
# Node joins
	pax -s 3 -c 1 -r 10 -l WARN --sched "{33,A,sjoin}"
	pax -s 3 -c 1 -r 10 -l WARN --sched "{33,A,cjoin}"
	pax -s 3 -c 4 -r 20 -l WARN --sched "{140,A,sjoin}"
#Primary fails
	pax -s 4 -c 1 -r 10 -l WARN --sched "{67,4,die}"
# Primary fails early
	pax -s 4 -c 1 -r 20 -l WARN --sched "{5,4,die}"
	pax -s 5 -c 1 -r 10 -l WARN --sched "{100,2,die}"
# End mid-view change
	pax -s 5 -c 1 -r 67 -l WARN --sched "{100,2,die}"
	pax -s 5 -c 1 -r 100 -l WARN --sched "{100,2,die}"
# Server joins
	pax -s 3 -c 1 -r 20 -l WARN --sched "{98,A,sjoin}"
# Crank up client count
	pax -s 3 -c 4 -r 20 -l WARN --sched "{92,A,sjoin}"
# A primary dies first, then new primary dies:
	pax -s 5 -c 4 -r 20 -l WARN --sched "{132,5,die} {152,4,die}"
# Server joins during vc
	pax -s 3 -c 4 -r 20 -l WARN --sched "{4,A,sjoin}"
# Die and join
	pax -s 5 -c 4 -r 20 -l WARN --sched "{55,5,die} {75,A,sjoin}"
	pax -s 5 -c 4 -r 20 -l WARN --sched "{178,5,die} {198,A,sjoin}"
	pax -s 5 -c 4 -r 20 -l WARN --sched "{91,5,die} {111,A,sjoin}"
# Pause & unpause
	pax -s 5 -c 4 -r 20 -l WARN --sched "{20,5,pause}{145,5,unpause}{75,4,pause}{200,4,unpause}" --seed 1
	pax -s 5 -c 4 -r 20 -l WARN --sched "{20,5,pause}{185,5,unpause}{120,4,pause}{285,4,unpause}" --seed 2
	pax -s 5 -c 4 -r 20 -l WARN --sched "{20,5,pause}{185,5,unpause}{70,4,pause}{235,4,unpause}" --seed 1
	pax -s 5 -c 4 -r 20 -l WARN --sched "{20,5,pause}{145,5,unpause}{60,4,pause}{185,4,unpause}" --seed 6
	pax -s 5 -c 4 -r 20 -l WARN --sched "{20,5,pause}{185,5,unpause}{70,4,pause}{235,4,unpause}" --seed 8
	pax -s 5 -c 4 -r 20 -l WARN --sched "{20,5,pause}{145,5,unpause}{70,4,pause}{195,4,unpause}" --seed 9
# Lots of death
	pax -s 15 -c 4 -r 20 -l WARN --sched "{20,15,die}{65,14,die}{110,13,die}{155,12,die}{200,11,die}{245,10,die}{290,9,die}" --seed 30
	pax -s 15 -c 4 -r 20 -l WARN --sched "{20,15,die}{50,14,die}{80,13,die}{110,12,die}{140,11,die}{170,10,die}{200,9,die}" --seed 80
	pax -s 15 -c 4 -r 20 -l WARN --sched "{20,15,pause}{105,15,unpause}{75,14,pause}{160,14,unpause}{130,13,pause}{215,13,unpause}{185,12,pause}{270,12,unpause}{240,11,pause}{325,11,unpause}{295,10,pause}{380,10,unpause}{350,9,pause}{435,9,unpause}"
	pax -s 15 -c 4 -r 20 -l WARN --sched "{20,15,pause}{125,15,unpause}{80,14,pause}{185,14,unpause}{140,13,pause}{245,13,unpause}{200,12,pause}{305,12,unpause}"
	pax -s 15 -c 4 -r 20 -l WARN --sched "{20,15,die}{60,14,die}{100,13,die}{140,12,die}{180,11,die}{220,10,die}{260,9,die}" --seed 50
	pax -s 15 -c 4 -r 20 -l WARN --sched "{20,15,pause}{145,15,unpause}{60,14,pause}{185,14,unpause}{100,13,pause}{225,13,unpause}{140,12,pause}{265,12,unpause}{180,11,pause}{305,11,unpause}{220,10,pause}{345,10,unpause}{260,9,pause}{385,9,unpause}"
	pax -s 15 -c 4 -r 20 -l WARN --sched "{20,15,pause}{185,15,unpause}{120,14,pause}{285,14,unpause}{220,13,pause}{385,13,unpause}{320,12,pause}{485,12,unpause}{420,11,pause}{585,11,unpause}{520,10,pause}{685,10,unpause}{620,9,pause}{785,9,unpause}"
	pax -s 15 -c 4 -l WARN -r 20 --sched "{20,15,pause}{165,15,unpause}{80,14,pause}{225,14,unpause}{140,13,pause}{285,13,unpause}{200,12,pause}{345,12,unpause}{260,11,pause}{405,11,unpause}{320,10,pause}{465,10,unpause}{380,9,pause}{525,9,unpause}"
	pax -s 15 -c 4 -r 20 -l WARN --sched "{20,15,pause}{185,15,unpause}{70,14,pause}{235,14,unpause}{120,13,pause}{285,13,unpause}{170,12,pause}{335,12,unpause}{220,11,pause}{385,11,unpause}{270,10,pause}{435,10,unpause}{320,9,pause}{485,9,unpause}"
	pax -s 15 -c 4 -r 20 -l WARN --sched "{20,15,pause}{165,15,unpause}{115,14,pause}{260,14,unpause}{210,13,pause}{355,13,unpause}{305,12,pause}{450,12,unpause}{400,11,pause}{545,11,unpause}{495,10,pause}{640,10,unpause}{590,9,pause}{735,9,unpause}"
	pax -s 15 -c 4 -r 20 -l WARN --sched "{20,15,pause}{185,15,unpause}{85,14,pause}{250,14,unpause}{150,13,pause}{315,13,unpause}{215,12,pause}{380,12,unpause}{280,11,pause}{445,11,unpause}{345,10,pause}{510,10,unpause}{410,9,pause}{575,9,unpause}"
	pax -s 15 -c 4 -r 20 -l WARN --sched "{20,15,pause}{185,15,unpause}{105,14,pause}{270,14,unpause}{190,13,pause}{355,13,unpause}{275,12,pause}{440,12,unpause}{360,11,pause}{525,11,unpause}{445,10,pause}{610,10,unpause}{530,9,pause}{695,9,unpause}"

# Give different results run at command line and run command line > foo.txt
#pax -s 3 -c 4 -r 20 --sched "{149,A,sjoin}"
#pax -s 15 -c 4 -r 20 --sched "{20,15,pause}{185,15,unpause}{60,14,pause}{225,14,unpause}{100,13,pause}{265,13,unpause}{140,12,pause}{305,12,unpause}{180,11,pause}{345,11,unpause}{220,10,pause}{385,10,unpause}{260,9,pause}{425,9,unpause}"
#pax -s 15 -c 4 -r 20 --sched "{20,15,pause}{205,15,unpause}{90,14,pause}{275,14,unpause}{160,13,pause}{345,13,unpause}{230,12,pause}{415,12,unpause}{300,11,pause}{485,11,unpause}{370,10,pause}{555,10,unpause}{440,9,pause}{625,9,unpause}"
#pax -s 5 -c 4 -r 20 --sched "{20,5,pause}{185,5,unpause}{105,4,pause}{270,4,unpause}" --seed 2

.PHONY: dist

dist:
	rm -rf dist
	mkdir dist
	mkdir dist/lab-exec
	mkdir dist/lab-vc
	cp *.h *.cpp Makefile dist/lab-exec
	cp *.h *.cpp Makefile dist/lab-vc
	cp -r getoptpp dist/lab-exec
	cp -r getoptpp dist/lab-vc
	mv dist/lab-exec/paxos_vc_stub.cpp dist/lab-exec/paxos_vc.cpp
	mv dist/lab-exec/paxos_exec_stub.cpp dist/lab-exec/paxos_exec.cpp
	rm dist/lab-vc/paxos_exec_stub.cpp
	mv dist/lab-vc/paxos_vc_stub.cpp dist/lab-vc/paxos_vc.cpp

.PHONY: clean
clean:
	rm -f pax.exe pax pax.dbg pax.dbg.exe *.o *.og *.d *.stackdump
	rm -rf dist

.PHONY: wc
wc:
	wc -l node.{h,cpp} log.{h,cpp} dssim.{h,cpp} version.{h,cpp} \
    word_vec_pax.{h,cpp} paxobj.h make_unique.h net.{h,cpp} main.cpp \
    paxos_print.cpp args.{h,cpp} paxclient.{h,cpp} paxserver.{h,cpp} \
    paxmsg.{h,cpp} paxlog.{h,cpp} paxtypes.h paxos_vc.cpp paxos_exec.cpp
