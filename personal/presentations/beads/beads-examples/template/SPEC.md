# Shop cart with discount codes (spec)

Build a small Java 21 online shop with a web page. Package: `com.example.shop`.
Build: Maven. Tests: JUnit 5. Money is always `BigDecimal` with scale 2, rounding `HALF_UP`.
No new dependencies: the `pom.xml` stays as it is (JUnit only). The web server uses the JDK's
built-in `com.sun.net.httpserver`.

## Features

1. Catalog (`Catalog.java`):
    - `static BigDecimal getPrice(String sku)`.
      Products: apple 0.50, bread 2.25, milk 1.80. Unknown SKU throws `IllegalArgumentException`.
    - `static Map<String, BigDecimal> all()`: all products in this order: apple, bread, milk.
      The map is read-only (the web page lists the products from it).

2. Discount codes (`Discounts.java`): `static BigDecimal apply(BigDecimal subtotal, String code)`.
   `SAVE10` = 10% off. `FIVEOFF` = 5.00 off, never below 0. Unknown code throws `IllegalArgumentException`.

3. Cart (`Cart.java`), uses the catalog and the discount codes:
   `add(String sku, int qty)`, `subtotal()`, `total(String code)` (code can be null).
   Quantity must be greater than 0, otherwise `IllegalArgumentException`.

4. Web (`WebServer.java`, `Main.java`, `src/main/resources/static/index.html`), uses the cart.
   It is the last feature: it waits for the cart.
    - `WebServer`: `static HttpServer start(int port)` creates the server, starts it and returns it.
      Port 0 means "any free port" (the tests use it; read the real port with
      `server.getAddress().getPort()`).
    - `Main`: `main(String[] args)` starts the server on `args[0]` or 8080 if there is no argument,
      and prints `Shop running on http://localhost:<port>`.
    - Routes (all responses are UTF-8):
        - `GET /` returns `index.html` from the classpath (`text/html`).
        - `GET /api/products` returns JSON like
          `[{"sku":"apple","price":"0.50"},{"sku":"bread","price":"2.25"},{"sku":"milk","price":"1.80"}]`.
        - `GET /api/quote?apple=2&milk=1&code=SAVE10` builds a `Cart` from the SKU parameters
          (every parameter except `code` is a SKU, its value is the quantity; `code` is optional)
          and returns `{"subtotal":"2.80","total":"2.52"}`. Without a `code` the total equals the subtotal.
          Without any SKU the result is `{"subtotal":"0.00","total":"0.00"}`.
        - Any `IllegalArgumentException` (unknown SKU, quantity 0 or not a number, unknown code) returns
          status 400 and `{"error":"<message>"}`. Any other path returns 404.
    - Write the JSON by hand with `String.format` (no JSON library). Quote and escape the text values.
    - The page `index.html` is one file with plain HTML, CSS and JavaScript. No external files,
      no CDN, no framework. It must:
        - show the title "Shop", and the products from `/api/products` (name, price, and a number input
          for the quantity, starting at 0);
        - have a text field for the discount code, with the hint "Try SAVE10 or FIVEOFF";
        - call `/api/quote` and update the result every time a quantity or the code changes,
          showing "Subtotal" and "Total" with the `$` sign, in big readable text (at least 20px);
        - show the error text in red when the API returns an error, and clear it when it is fine;
        - look clean: centered card, system font, spacing, a light background.
    - Tests (`WebServerTest.java`) start the server on port 0 and call it with `java.net.http.HttpClient`.
      Cover: `/api/products`, a quote without a code, `SAVE10`, `FIVEOFF` that never goes below 0,
      an unknown SKU (400), an unknown code (400), quantity 0 (400), an unknown path (404),
      and `GET /` (status 200, contains `<title>Shop</title>`). Stop the server after each test.

## Rules

- Java code goes in `src/main/java/com/example/shop/`. Static files go in `src/main/resources/static/`.
- Each feature has its own test class in `src/test/java/com/example/shop/`
  (for example `CartTest.java`, `WebServerTest.java`).
- Run all tests with: `mvn -q test`
- Do not start the server by hand to test it: the tests start it. Do not change `pom.xml`.

## How to run the shop (for the human, after the build)

```bash
mvn -q compile
java -cp target/classes com.example.shop.Main
```

Then open http://localhost:8080
