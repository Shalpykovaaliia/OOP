<h1 align="center">LIbrary Management System  for JARS </h1>



Features
-------------------
  * Manage Borrower
    - add
      * allow using webcam to capture profile picture
    - delete 
    - edit
    - list borrowers

  * Manage book record
    - add 
    - view
      * show status of book (borrowed or avaibale)
        if available
      * Show barcode output
      * Print barcode 
    - list books
      * show book information and availability
    - edit
    - delete
    - borrow
    - search and filter
    - audit 
      * show history of who borrowed what books
  * Notify user when the book is due
    - allow setting of time of the day
  * Notify admin when credits run out 

  * System user registration form

  * Registration form for end user

  * Application Settings
      dynamic setting (
        Number of allowed books to borrow Penalty, 
        number of allowable days
  * Scan barcode 
    - of books and show book information
    - of borrower/library personel id and show person information
  * Penalty Information
  * Returned Book monitor
  * Borrowed Books monitoring
  * Penalty Report by date range
    - show list of people that paid penalty and show its total collected
  * Print barcode image
  * Show list of borrowed books



Tables
------------

Database Schema


tbl_books
book_id
ISBN
availability
title
author
description
edition
edition_year
date_created
date_updated

tbl_category
category_id
title
description
date_created
date_updated

tbl_book_category
book_id
category_id

tbl_borrower
borrower_id
title
firstname
lastname
birthday
gender
address 1
address 2
address 3
postal_code
town
country
mobile_number
email_address


tbl_book_borrower
id
book_id
borrower_id
date_returned
expected_return_date
date_borrowed
notes


tbl_book_overdue
book_borrower_ref_id
computed_fee
paid
balance
notes

tbl_user
id
username
password
first_secret_question
first_secret_answer
second_secret_question
second_secret_answer
third_secret_question
third_secret_answer

tbl_profile
profile_id
firstname
lastname
email_address
phone_number


tbl_user_profile
user_id
profile_id



Actor
------------
	Administrator
	Librarian
	Borrower





















@TODO - 
loading javafxml problem 


@TODO - load all javafxml on load 

@TODO  - create map that will hold the application view collection 
to be called later in the application