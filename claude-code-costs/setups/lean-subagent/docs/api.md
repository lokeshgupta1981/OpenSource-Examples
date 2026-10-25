# OrderDesk REST API


### POST /orders

Create an order. Responses: 201 with the order, 400 for invalid input.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

### GET /orders/{id}

Read one order. Responses: 200 or 404.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

### GET /orders

List orders, paged with ?page and ?size. Responses: 200.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

### POST /orders/{id}/confirm

Confirm a new order. Responses: 200, 409 if not NEW.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

### POST /orders/{id}/cancel

Cancel an order. Responses: 200, 409 if already shipped.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

### POST /orders/{id}/ship

Mark an order as shipped. Responses: 200, 409 if not CONFIRMED.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

### GET /orders/{id}/invoice

Download the invoice as PDF. Responses: 200 application/pdf.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

### GET /customers/{id}/orders

Orders for one customer. Responses: 200.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

### GET /inventory/{sku}

Reserved and available stock for a SKU. Responses: 200 or 404.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

### POST /inventory/{sku}/restock

Add stock. Responses: 204.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

### GET /coupons

Active coupon codes. Responses: 200.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

### POST /coupons

Create a coupon. Responses: 201, 400 for duplicate code.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

### DELETE /coupons/{code}

Retire a coupon. Responses: 204.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

### GET /reports/daily

Daily sales total. Responses: 200.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

### GET /reports/top-skus

Top 10 SKUs this week. Responses: 200.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

### GET /health

Liveness check. Responses: 200.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

