#include "args.h"
#include <unordered_map>
#include "getoptpp/getopt_pp.h"

void usage(const char* exe_name) {
   printf("%s: A Paxos simulator\n", exe_name);
   printf("\t-c --clients <int> Number of clients [3]\n");
   printf("\t-r --requests <int> Number of words per client [500]\n");
   printf("\t-s --servers <int> Number of servers [3]\n");
   printf("\t-l --log_level DEBUG|INFO|SHORT|WARN|EMERG Logging level [SHORT]\n");
   printf("\t-f --fake_init_vc use fake initial view change\n");
   printf("\t--client_timo <int> Client timeout for request (>=1.5x nclients) [100]\n");
   printf("\t--sched {T, N, A},{... tick, node, action (die|join|pause|unpause) triples []\n");
   printf("\t--outfile <fname>|stdout|stderr a file for logging output [stdout]\n");
   printf("\t--delay <0-100> probability a recv batch (of 3) is delayed [10]\n");
   printf("\t--shuffle <0-100> probability a message queue is shuffled [20]\n");
   printf("\t--seed <int> the random seed [100]\n");
}

std::unordered_map<std::string,l::level> s2level = {
   {"EMERG", l::EMERG},
   {"WARN",  l::WARN},
   {"SHORT", l::SHORT},
   {"INFO",  l::INFO},
   {"DEBUG", l::DEBUG},
};
using namespace GetOpt;
void do_args(int argc, char* argv[], dssim_t::Config& con) {
   try {
      GetOpt_pp ops(argc, argv);
      ops.exceptions(std::ios::failbit | std::ios::eofbit);

      ops >> Option('c', "clients", con.nclients, 3);
      ops >> Option('r', "requests", con.nclient_req, 500);
      ops >> Option('s', "servers", con.nservers, 3);
      ops >> Option('l', "log-level", con.log_level, "SHORT");
      ops >> OptionPresent('f', "fake_init_vc", con.fake_init_vc);

      // No short version
      ops >> Option("client_timo", con.client_timo, 100);
      if(2 * con.client_timo < con.nclients) {
         std::cerr << "XXX Possible misconfiguration!\n"
                   << "Number of clients " << con.nclients
                   << " client timo " << con.client_timo
                   << "\nClient timeout should be greater than about 1/2 nclients or you risk livelock\n"
                   << "and that is at 3 batch, 10% delay & 20% shuffle\n\n";
      }
      ops >> Option("sched", con.str_sched, "");
      con.sched.init(con.str_sched);
      ops >> Option("outfile", con.outfile, "stdout");
      ops >> Option("delay", con.prob_delay, 10);
      ops >> Option("shuffle", con.prob_shuffle, 20);
      ops >> Option("seed", con.rand_seed, 100);

      if (ops >> OptionPresent('h', "help")) {
         usage(argv[0]);
         exit(0);
      }

      if (ops.options_remain()) {
         std::cerr << "Unknown options\n";
         usage(argv[0]);
         exit(17);
      }
   }
   catch( const GetOptEx& e) {
      std::cerr << "Invalid options\n";
         usage(argv[0]);
         exit(16);
   }
   if(s2level.count(con.log_level) == 0) {
      std::cerr << "Bad log level " << con.log_level << std::endl;
      usage(argv[0]);
      exit(71);
   }
   // Handle logging here, not in dssim, because a program needs to log
   l::set_log_level(s2level[con.log_level]);
   l::set_ofname(con.outfile);

   //////////////////////////////////////////////
   // These options have no command line switch yet
   con.client_switch_ntimo = 3;
   con.serv_vca_timo = 50;
   con.serv_dead_timo = 200;
   if(con.serv_dead_timo <= con.client_timo) {
      std::cerr << "XXX Possible misconfiguration!\n"
                << "Client timo " << con.client_timo
                << " Server dead timo " << con.serv_dead_timo
                << "\nServer timeout should be bigger than client\n";
   }
   // Less than client timo, right?
   con.serv_heartbeat_timo = 75;
   // XXX Should allow user to set timeout randomization interval
}
