#include<sys/time.h>
#include<time.h>
#include<stdarg.h>
#include <cstdio>
#include <fstream>

#include <mutex>

#include "log.h"
#include "version.h"

static std::string out_fname = "dslog.txt";
static std::ostream* logf = nullptr;
static std::ofstream dummyf;
static std::mutex _mutex;
l::level l::log_level = l::DEBUG;
unsigned int l::log_mask = 0x00000FF; // Print all
//unsigned int log_mask = 0xB0000FF;  // Print neither NORM nor VC
#define BUF_SZ 1024
static char buf[BUF_SZ];


void l::set_ofname(std::string s) {
   out_fname = s;
}

void l::set_log_level(l::level ll) {
   l::log_level = ll;
}

static void ctor() {
   if(out_fname == "stdout") {
      logf = &std::cout;
   } else if(out_fname == "stderr") {
      logf = &std::cerr;
   } else {
      std::ofstream *myf = new std::ofstream();
      myf->open (out_fname , std::ofstream::out|std::ofstream::trunc);
      if (!myf->is_open()) {
         perror ("Error opening log file");
         logf = &dummyf;
         return;
      }
      logf = myf;
   }
   // Only print time and build info for "long form" output
   if(l::log_level > l::WARN) {
      timeval now;
      gettimeofday(&now, NULL);
      // Convert now to tm struct for local timezone
      tm* ltm = localtime(&now.tv_sec);
      snprintf(buf, BUF_SZ, "%4d-%02d-%02d %02d:%02d:%02d_%03d\n",
               1900+ltm->tm_year, 1+ltm->tm_mon, ltm->tm_mday,
               ltm->tm_hour, ltm->tm_min, ltm->tm_sec,
               (int)(now.tv_usec/1000));
      *logf << buf;
      snprintf(buf, BUF_SZ, "\tLink time: %s\n", link_time);
      *logf << buf;
      snprintf(buf, BUF_SZ, "\t%s\n", build_git_sha);
      *logf << buf;
   }
}

static bool log_ok() {
   if(logf == nullptr) {
      std::unique_lock<std::mutex> lock(_mutex);
      ctor();
   }
   if(!logf->good()) return false;
   return true;
}

void l::og(l::level _log_level, const char* format, ...) {
   if(!log_ok()) return;

   if(_log_level <= l::log_level) {
      // Timestamps clutter output for limited value
#if 0
      timeval now;
      gettimeofday(&now, NULL);
      // Convert now to tm struct for local timezone
      tm* ltm = localtime(&now.tv_sec);
      if(_log_level == DEBUG) {
         snprintf(buf, BUF_SZ, "%02d:%02d:%02d_%04d    ",
                      ltm->tm_hour, ltm->tm_min, ltm->tm_sec,
                      (int)(now.tv_usec/100));
         *logf << buf;
      } else {
         snprintf(buf, BUF_SZ, "%02d:%02d:%02d_%04d ",
                      ltm->tm_hour, ltm->tm_min, ltm->tm_sec,
                      (int)(now.tv_usec/100));
         *logf << buf;
      }
#endif
      va_list args;
      va_start(args, format);
      vsnprintf(buf, BUF_SZ, format, args);
      va_end(args);
      *logf << buf;
      logf->flush();
   }
}

std::ostream& l::og(l::level _log_level) {
   if(!log_ok()) return dummyf;

   if(_log_level <= l::log_level) {
      // Omits timestamp
      return *logf;
   }
   return dummyf;
}


