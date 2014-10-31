
gcc is broken!

#if 0
// Grr, gcc (as 4.8.3) doesn't really support regex
void Sched::init(std::string& str) {
// Delimiters are spaces (\s) and/or commas
  std::regex e ("[\\s,]+)"); 
  std::regex_iterator<std::string::iterator> rit ( str.begin(), str.end(), e );
  std::regex_iterator<std::string::iterator> rend;

  while (rit!=rend) {
    std::cout << rit->str() << std::endl;
    ++rit;
  }
//   std::regex re("[\\s,]+");
//   std::sregex_token_iterator it(str.begin(), str.end(), re);//, -1);
//   const std::sregex_token_iterator reg_end;
//   for (; it != reg_end; ++it) {
//      std::cout << it->str() << std::endl;
//   }
}

void Sched::init(const std::string& str) {
   std::size_t open, close;
//   const char * re_str = "([0-9]+)\\s*,\\s*([0-9A-Z]+)\\s*"; //,\\s*(die|cjoin|sjoin|pause|unpause)";
//   const char * re_str = "([0-9]+)[\\s,]*([0-9A-Z]+)[\\s,]*"; //,\\s*(die|cjoin|sjoin|pause|unpause)";
   const char * re_str = "7";
   fprintf(stderr, "%s\n", re_str);
   std::regex ev_re(re_str);
   open = str.find("{");
   close = str.find("}");
   while( open != std::string::npos
          && close != std::string::npos ) {
      // Cut open and close braces
      std::string segment = str.substr(open + 1, close-open-1);
      std::smatch ev_match;
                    std::cerr << "Searching--" << segment<<"--\n";
      if(std::regex_search(segment, ev_match, ev_re)) {
         MASSERT(ev_match.size() == 4, "Grammar");
         std::cerr << "Ti " << ev_match[1].str();
         std::cerr << "Ni " << ev_match[2].str();
         std::cerr << "Ai " << ev_match[3].str();
         open = str.find("{", close);
         close = str.find("}", open);
      } else {
         std::cerr << "Parse failure: " << segment << '\n';
         exit(99);
      }
   }
}
#endif

