INSERT INTO products
(
    name,
    description,
    price,
    stock_quantity,
    active,
    created_at,
    updated_at,
    category_id,
    image_url
)
VALUES

(
'Apple iPhone 16',
'Apple iPhone 16 128GB',
79999,
20,
TRUE,
NOW(),
NOW(),
1,
'https://images.unsplash.com/photo-1592286927505-1def25115558?w=600'
),

(
'Samsung Galaxy S25',
'Samsung flagship smartphone',
74999,
18,
TRUE,
NOW(),
NOW(),
1,
'https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=600'
),

(
'OnePlus 13',
'Fast flagship Android phone',
69999,
25,
TRUE,
NOW(),
NOW(),
1,
'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=600'
),

(
'MacBook Air M4',
'Apple M4 powered laptop',
119999,
10,
TRUE,
NOW(),
NOW(),
2,
'https://images.unsplash.com/photo-1517336714739-489689fd1ca8?w=600'
),

(
'Dell XPS 15',
'Premium Windows laptop',
149999,
7,
TRUE,
NOW(),
NOW(),
2,
'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=600'
),

(
'Sony WH-1000XM5',
'Noise cancelling headphones',
29999,
30,
TRUE,
NOW(),
NOW(),
3,
'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600'
),

(
'Apple AirPods Pro',
'Wireless earbuds',
24999,
40,
TRUE,
NOW(),
NOW(),
3,
'https://images.unsplash.com/photo-1600294037681-c80b4cb5b434?w=600'
),

(
'Apple Watch Series 10',
'Premium smartwatch',
45999,
15,
TRUE,
NOW(),
NOW(),
4,
'https://images.unsplash.com/photo-1434493789847-2f02dc6ca35d?w=600'
),

(
'Samsung Galaxy Watch',
'Android smartwatch',
27999,
12,
TRUE,
NOW(),
NOW(),
4,
'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600'
),

(
'Logitech MX Master 3S',
'Wireless productivity mouse',
8999,
50,
TRUE,
NOW(),
NOW(),
5,
'https://images.unsplash.com/photo-1527814050087-3793815479db?w=600'
);