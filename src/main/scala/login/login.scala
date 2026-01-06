package login

import scala.collection.mutable

case class User(username: String, password: String)

object AuthSystem {
  val users = mutable.ListBuffer(
    User("alice", "secure123"),
    User("bob", "password456")
  )
  val attempts = mutable.Map[String, Int]()

  def authenticate(username: String, password: String): Boolean = {
    recordAttempt(username)

    if isBlocked(username) then return false

    users.find(_.username == username) match {
      case Some(user) =>
        resetAttempts(username)
        password == user.password
      case None => false
    }
  }

  def register(username: String, password: String): Boolean = {
    if users.exists(_.username == username) then return false

    users += User(username, password)
    resetAttempts(username)
    true
  }

  def recordAttempt(username: String): Unit = {
    val current = attempts.getOrElse(username, 0)
    attempts(username) = current + 1
  }

  def isBlocked(username: String): Boolean = {
    attempts.getOrElse(username, 0) >= 5
  }

  def resetAttempts(username: String): Unit = {
    attempts.remove(username)
  }
}
