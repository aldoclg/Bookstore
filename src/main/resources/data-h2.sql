

INSERT INTO CustomerS (
    Customer_ID,
    LOYALTY_POINTS
)
values(0, 10),(1, 6),(2, 0),(3, 5),(4, 10);


INSERT INTO TYPES (
    TYPE_ID,
    NAME
)
values
(0, 'NEW_RELEASES'),
(1, 'REGULAR'),
(2, 'OLD_EDITIONS');

INSERT INTO BOOKS (
    BOOK_ID,
    TYPE_ID,
    NAME,
    PRICE,
    AMOUNT
)
values
(0, 2, 'BOOK 1', 12.00, 12),
(1, 2, 'BOOK 2', 42.00, 32),
(2, 1, 'BOOK 3', 21.00, 10),
(3, 0, 'BOOK 4', 12.00, 5);