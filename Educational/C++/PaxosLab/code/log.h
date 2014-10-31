#pragma once
#include <iostream>

class l {
  public:
   // Picking from RFC 5424
   enum level {
      EMERG,
      WARN,
      SHORT, // Try to keep output to about a page
      INFO,
      DEBUG,
      // Flags on debug levels
      DBG_NORM = (DEBUG),// | 0x8000000),
      DBG_VC   = (DEBUG),// | 0x4000000)
      DBG_EV   = (DEBUG),
   };
   static level log_level;
   // Allows filtering of types of log messages
   static unsigned int log_mask;

   // Call this before logging to set output file name
   static void set_ofname(std::string);
   static void set_log_level(l::level);
   static void og(l::level _log_level, const char* format, ...);
   static std::ostream& og(l::level _log_level);
//   template<typename... Args>
//   static void og(int _logLevel, const char* format, const Args&... args);

  private:
   l() {};     // Empty constructor
   // No copy
   l(l const&);              // Don't Implement
   void operator=(l const&); // Don't implement
};

//#define LOG(a, b) if(((a)&l::log_mask) <= l::log_level){l::og(a) << b;}
#define LOG(a, b) if((a) <= l::log_level){l::og(a) << b;}
