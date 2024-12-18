<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Invoice</title>
  <style>
    body {
      font-family: Arial, sans-serif;
    }
    .invoice {
      max-width: 600px;
      margin: auto;
      padding: 20px;
      border: 1px solid #ccc;
      border-radius: 8px;
    }
    .invoice h1 {
      text-align: center;
      color: #007BFF;
    }
    .details {
      margin-top: 20px;
    }
    .details p {
      margin: 5px 0;
    }
  </style>
</head>
<body>
<div class="invoice">
  <h1>Invoice</h1>
  <div class="details">
    <p><strong>Name:</strong> ${name}</p>
    <p><strong>Amount:</strong> ${amount}</p>
    <p><strong>Date:</strong> ${date}</p>
  </div>
</div>
</body>
</html>
