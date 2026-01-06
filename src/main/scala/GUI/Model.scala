package model

import scripts._

/** Model for the MVC architecture, which exposes only the Menu object to the
  * View and the Controller. Menu coordinates use of all other model classes.
  */
class Model {
  val menu = new Menu
}
