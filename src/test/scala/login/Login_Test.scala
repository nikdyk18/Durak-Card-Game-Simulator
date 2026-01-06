package login

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._

class AuthSystemTest extends AnyFunSpec with Matchers {

  describe("the OWASP login system") {
    it("should authenticate valid credentials") {
        assert(AuthSystem.authenticate("alice", "secure123") === true)
    }

    it("should reject invalid password") {
        assert(AuthSystem.authenticate("alice", "wrongpassword") === false)
    }

    it("should reject nonexistent user") {
        assert(AuthSystem.authenticate("charlie", "any") === false)
    }

    it("should reject brute-force attacks after 5 failed attempts") {
        AuthSystem.resetAttempts("alice")
        for i <- Range(0,5) do 
          AuthSystem.authenticate("alice", "wrongpassword")
        assert(AuthSystem.authenticate("charlie", "secure123") === false)
    }

    it("should accept a username after 5 failed attempts IF the username's failed attempts are reset") {
        AuthSystem.resetAttempts("alice")
        for i <- Range(0,5) do 
          AuthSystem.authenticate("alice", "wrongpassword")
        assert(AuthSystem.authenticate("charlie", "secure123") === false)
        AuthSystem.resetAttempts("alice")
        assert(AuthSystem.authenticate("charlie", "secure123") === false)


    }
  }
}