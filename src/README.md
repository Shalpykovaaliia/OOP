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
@DONE

@TODO - load all javafxml on load 
@DONE
@TODO  - create map that will hold the application view collection 
to be called later in the application
@DONE

@TODO - manage book record
bug on after updating the record . the list should contain the update records. 
its not updating record
@DONE



@TODO 
borrow book
    view - done
    logic -done
@TODO - after borrow , the status of book should be unavailable
@DONE

@TODO - show all borrowed books
@DONE

@TODO - show all borrowers
@DONE

@TODO
dashboard put button allowing user to borrow book . 
@DONE

@TODO 
return book
    view -  @DONE
    logic  - 



@TODO  - expected return date is not set 
@DONE    

@TODO - after login set the CUrrent User Login object.
@DONE

@TODO - on dashboard open . load the current username and its role.
@DONE


@TODO - not created Over Due record after returning the book
@DONE


@TODO - book overdue will only be created  after paying the fee or after returning the book
@DONE


@TODo - when returning book. no overdue record is created
@DONE

@TODO - overdue record is still created even if there book is not overdue
@DONE


@TODO - show overdue day of a book instead of edition
@DONE

@TODO - after returniong the books . and searching the name of the borrower . 
it should show "This borrower didnt borrow any books or has returned the books he/she borrowed.";
@DONE


@TODO - when borrowing books. nadodoble yung last na bookborrow na record
@DONE

@TODO - LibraryManagementSystem.java - continue the line with @TODO

@TODO - on load of librarymanagement system class . 
check setting is present. if not yet there . create the setting 
if setting record is there . dont update

@TODO - SMS_API_CODE

@TODO - SMS_SENDER_NAME

@TODO - NOTIFICATION_STATUS  [enabled,disabled]

@TODO  - overdued books notification . notify evertyime system boots up. 
only once. Overdued books should have a log if it is notified via SMS or not. 
To prevent double sending
there should be a log that will log borrowers id and book id .
and has a status of sent or not . and the date the notifiction is sent


=====================
NICE Things to Have
=====================
@TODO - allow user to print barcode image . put toolbox button that when clicked it prints all barcode image of books

@TODO - a button that opens list of books and their availability. allow user to search partial/filter by book name 


@TODO - dasboard button that opens return book panel

@TODO - thread that will overdue books and then invoke system notification showing "an overdue book is detected" or something like that
      - attach SMS notifier


@TODO - Settings panel that reports number of sms credits left.

Nice things to have

@TODO - detect database configuration. allow user to set database configuration


