# WolfCafe

![Coverage](.github/badges/jacoco.svg)
![Branch Coverage](.github/badges/branches.svg)

All required enhancements have been implemented as specified in the [problem statement](https://pages.github.ncsu.edu/engr-csc326-staff/326-course-page/team-project/problem-stmt)

## Extra Credit completed

* Coverage badges: Line and branch coverage badges generated and updated for each build. The build will fail at below 70%/50% and badges will be green at 80%/60% respectively
* Additional User Role: Instead of Staff, we now have a Barista that can add inventory and fulfill orders, and a Manager that can do these things plus create, edit, and delete items and view a revenue graph.
* Anonymous Orders: On the login page, there is a Continue As Guest button that can be used to place an order without customer registration; a new unique guest account will automatically be created every time someone clicks this button, with the GUEST role that gives access to customer actions. Their order history will be saved with the login session, so they will be able to view and mark orders as picked up, but on a subsequent login it will reset.
* Order History: Customers can view their own order history, and staff can see all order history (this is integrated with the fulfill and pick up functionality). Managers can also view a revenue graph as an extra visualization.
