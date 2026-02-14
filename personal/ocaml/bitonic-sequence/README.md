# Bitonic Sequence Generator (OCaml)

A complete OCaml implementation of a bitonic sequence generator with REST API and Redis caching.

## Overview

This project generates a **bitonic sequence** of size `N` from integers in a given range `[L, R]`.

A bitonic sequence is one that first increases and then decreases. For example: `[1, 3, 5, 4, 2]`.

**Reference:** [GeeksforGeeks - Generate Bitonic Sequence](https://www.geeksforgeeks.org/generate-bitonic-sequence-of-length-n-from-integers-in-a-given-range/)


## Quick Start

```powershell
# Install dependencies
opam install . --deps-only --with-test

# Build
opam exec -- dune build

# Run tests
opam exec -- dune runtest

# Start Redis
docker-compose up -d

# Run server
opam exec -- dune exec ./bin/main.exe

# Run k6 tests
k6 run k6/load_test.js
```

The server will start and listen on `http://0.0.0.0:8080` (accessible via `http://localhost:8080`)

API available at `http://localhost:8080`

## API Usage

### Generate bitonic sequence (POST)

Example (Windows cmd or PowerShell):

```cmd
curl.exe -X POST http://localhost:8080/bitonic -H "Content-Type: application/json" -d '{"n":5,"min":3,"max":10}'
```

Response example:

```json
{"sequence":[9,10,9,8,7],"length":5,"is_bitonic":true}
```

### Health check (GET)

```cmd
curl.exe http://localhost:8080/health
```

Response example:

```json
{"status":"ok"}
```


**Parameters:**
- `n` - Length of sequence
- `min` - Lower bound (inclusive)
- `max` - Upper bound (inclusive)

## Load Test (k6)

A load test script is available in `k6/load_test.js` using [k6](https://k6.io/).

### How to run the load test

1. Make sure the server is running (`opam exec -- dune exec ./bin/main.exe`).
2. Install k6: https://k6.io/docs/getting-started/installation/
3. Run the test:

```powershell
k6 run k6/load_test.js
```

At the end, an HTML report will be generated as `summary.html`.

The test sends POST requests to `http://localhost:8080/bitonic` with the following payload:

```json
{
  "n": 100,
  "min": 1,
  "max": 1000
}
```