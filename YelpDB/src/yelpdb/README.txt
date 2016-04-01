Timothy Phan
CECS521 - Homework #3

Compilation and running instructions:
javac -cp .;ojdbc6.jar;gson-2.6.2.jar populate.java hw3.java
java -cp \*; populate yelp_business.json yelp_review.json yelp_checkin.json yelp_user.json
java -cp \*; hw3


Notes:
The program only checks the "From" time because I was unsure how I would handle the "To" time.
I use the 24-hour clock and I was unsure how to compare the "To" time to businesses that close after
23:00 hours. In order to display the sub-categories, the sub-categories must be associated with all the
selected main categories. In order to display the attributes, the attributes can be associated with
any selected subcategories. In order to display the businesses, the business must contain at least
one of the selected sub-categories and depending on the option selected any/all attributes selected.