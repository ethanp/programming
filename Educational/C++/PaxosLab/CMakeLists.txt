cmake_minimum_required(VERSION 2.8.4)
project(PaxosLab)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES
    code/getoptpp/examples/example_env.cpp
    code/getoptpp/examples/example_extendingtypes_date.cpp
    code/getoptpp/examples/example_formatflags.cpp
    code/getoptpp/examples/example_general1.cpp
    code/getoptpp/examples/example_global_option.cpp
    code/getoptpp/examples/example_helloworld.cpp
    code/getoptpp/examples/example_helloworld_ex.cpp
    code/getoptpp/examples/example_iterators.cpp
    code/getoptpp/examples/example_nomanip.cpp
    code/getoptpp/examples/example_nomanip_ex.cpp
    code/getoptpp/getoptpp/getopt_pp.h
    code/getoptpp/getoptpp/getopt_pp_standalone.h
    code/getoptpp/src/getopt_pp.cpp
    code/getoptpp/tests/format_test.cpp
    code/getoptpp/tests/simpleoptions_test.cpp
    code/args.cpp
    code/args.h
    code/dssim.cpp
    code/dssim.h
    code/log.cpp
    code/log.h
    code/main.cpp
    code/make_unique.h
    code/massert.h
    code/md5.cpp
    code/md5.h
    code/net.cpp
    code/net.h
    code/node.cpp
    code/node.h
    code/parse_regex.cpp
    code/paxclient.cpp
    code/paxclient.h
    code/paxlog.cpp
    code/paxlog.h
    code/paxmsg.cpp
    code/paxmsg.h
    code/paxobj.h
    code/paxos_exec.cpp
    code/paxos_print.cpp
    code/paxos_vc.cpp
    code/paxserver.cpp
    code/paxserver.h
    code/paxtypes.h
    code/t.cpp
    code/version.cpp
    code/version.h
    code/word_vec_pax.cpp
    code/word_vec_pax.h
    "AOS Lab4 Paxos Notes.md")

add_executable(PaxosLab ${SOURCE_FILES})