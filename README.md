# WolfCafe

![Coverage](.github/badges/jacoco.svg)
![Branch Coverage](.github/badges/branches.svg)

All required enhancements have been implemented as specified in the [problem statement](https://pages.github.ncsu.edu/engr-csc326-staff/326-course-page/team-project/problem-stmt)

## Extra Credit completed

* Coverage badges: See above, and the build status should change if coverage is insufficient.
* Additional User Role: Instead of Staff, we now have a Barista that can add inventory and fulfill orders, and a Manager that can do these things plus create, edit, and delete items and view a revenue graph.
* Anonymous Orders: On the login page, there is a Continue As Guest button that can be used to place an order without customer registration; a new unique guest account will automatically be created every time someone clicks this button, with the GUEST role that gives access to the place and pick up order functionality.
* Order History: Customers can view their own order history, and staff can see all order history (this is integrated with the fulfill and pick up functionality). Managers can also view a revenue graph as an extra visualization.
