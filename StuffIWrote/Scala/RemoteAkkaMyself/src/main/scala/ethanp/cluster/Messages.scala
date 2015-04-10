package ethanp.cluster

/**
 * Ethan Petuchowski
 * 4/9/15
 */
case class ChatRequest(text: String)
case class ChatResult(text: String)
case class JobFailed(reason: String, job: ChatRequest)
case object ServerRegistration
case object MasterRegistration
case class ChatLog(string: String)

case class IDAssignment(id: Int)
