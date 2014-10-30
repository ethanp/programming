#include <iomanip>
#include "paxlog.h"
#include "paxclient.h"
#include "paxserver.h"

//////////////////////////////////////////////////////
// Paxlog
std::ostream& operator<<(std::ostream& os, const Paxlog::tup& tup) {
   os << "PLE " << std::setw(2) << tup.vs;
   if(tup.request) {
      os << " " << tup.request->name;
   }
   os << " src:" << tup.src
      << " rid:" << tup.rid
      << " scnt:" << tup.serv_cnt
      << " rcnt:" << tup.resp_cnt
      << " exec:" << tup.executed
      ;
   return os;
}
std::ostream& operator<<(std::ostream& os, const Paxlog& plog) {
   if(plog.l.size() == 0) {
      os << "PAXLOG empty";
      return os;
   }
   os << "PAXLOG: " << plog.l.size() 
      << " last_exec:" << plog.last_exec_vs;
   for(auto entry = plog.l.begin(); entry != plog.l.end(); ++entry) {
      os << "\n\t";
      if(*entry) os << **entry;
      else os << "null entry ";
   }
   os.flush();
   return os;
}

//////////////////////////////////////////////////////
// Paxobj
// Useful for debugging
std::ostream& operator<<(std::ostream& os, const paxobj& po) {
   os << po.to_str();
   return os;
}

//////////////////////////////////////////////////////
// Messages & primitive data types

std::ostream& operator<<(std::ostream& os, const viewid_t& v) {
   os << "[cnt:" << v.counter;
   os << " mgr:" << v.manager << "]";
   return os;
}
std::ostream& operator<<(std::ostream& os, const view_t& view) {
   os << "(" << view.vid << " " << " pr:" << view.primary;
   os << " bk:";
   for(auto backup : view.backups) {
      os << " " << backup;
   }
   os << ")";
   return os;
}
std::ostream& operator<<(std::ostream& os, const viewstamp_t& vs) {
   os << "[ts:" << vs.ts;
   os << " " << vs.vid << "]";
   return os;
}

std::ostream& operator<<(std::ostream& os, const net_msg_t& msg) {
   msg.pr(os);
   return os;
}
//////////////////////////////////////////////////////
// paxserver and paxclient

std::ostream& operator<<(std::ostream& os, const vc_mgr_t& vcmgr) {
   os << "VCMGR old_pr: " << vcmgr.old_pr_in 
      << " annou_nv:"<< vcmgr.announce_nv
      << " serv_cnt:" << vcmgr.serv_cnt 
      << "\n";
   os << "   latest:" << vcmgr.latest
      << " start:" << vcmgr.start
      << " last:" << vcmgr.last_resp 
      << "\n";
   os << "   VCA: ";
   for(auto& acc : vcmgr.vca) {
      acc->pr(os);
   }
   os << "\n   NVR: " << vcmgr.nvr.size();
   return os;
}
std::ostream& operator<<(std::ostream& os, const vc_state_t& vcs) {
   const char* state_name[] = {"ACTIVE", "MANAGER", "UNDERLING"};
   os << "vc_state: " << state_name[vcs.mode] << "\n";
   os << "\tview: " << vcs.view << "\n";
   os << "\tlatest: " << vcs.latest_seen 
      << " proposed " << vcs.proposed_vid << "\n";
   if(vcs.accepted_view==nullptr) {
      os << "\taccepted: NULL";
   } else {
      os << "\taccepted: " << *vcs.accepted_view;
   }
   return os;
}
// XXX incomplete
std::ostream& paxserver::pr_allstate(std::ostream& os) {
   if(vc_state.mode == vc_state_t::ACTIVE) {
      if(vc_state.view.primary == nid) {
         os << nid << " PRIMARY ";
         int i = 0;
         int wid = os.width();
         char fill = os.fill();
         os.fill('0');
         os.width(2);
         os << "B: ";
         for(auto b = vc_state.view.backups.cbegin(); 
             b != vc_state.view.backups.cend(); ++b) {
            os << '(' << i << "," << *b << ") ";
            ++i;
         }
         os << std::endl;
         os.fill(fill);
         os.width(wid);
      } else {
         os << nid << " BACKUP ";
         os << "P(" << vc_state.view.primary << ")" << std::endl;
      }
      os << "vid counter(" << vc_state.view.vid.counter
         << ") manager(" << vc_state.view.vid.manager
         << ")\n";
   } else {
      os << "ERROR ";
   }
   
   return os;
}

std::ostream& paxserver::pr_stat(std::ostream& os) const {
   os << "\n\tPAXSERVER " << id_str();
   os << " Snd: " << std::setw(6) << net->sends(nid)
      << " Rcv: " << std::setw(6) << net->recvs(nid);

   os << "\n\tPR Start: "<< std::setw(6) << stat.pr_started_op 
      << " Succ: " << std::setw(6) << stat.pr_success_op;

   os << "\n\tBK Start: "<< std::setw(6) << stat.bk_started_op 
      << " Succ: " << std::setw(6) << stat.bk_success_op;

   os << "\n\tSyncwrite: " << std::setw(6) << stat.sync_writes;
   os << "\n\tMax paxlog: " << std::setw(6) << stat.max_paxlog;
   return os;
}

std::ostream& operator<<(std::ostream& os, const paxclient::req_rec& rr) {
   rr.pr(os);
   return os;
}
std::ostream& paxclient::pr_stat(std::ostream& os) const {
   os << "\n\tPAXCLIENT " << id_str();
   os << " Snd: " << std::setw(6) << net->sends(nid)
      << " Rcv: " << std::setw(6) << net->recvs(nid);
   os << " Start: "<< std::setw(5) << stat.started_op 
      << " Succ: " << std::setw(5) << stat.success_op;
   return os;
}
