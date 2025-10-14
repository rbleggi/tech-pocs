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
opam exec -- dune exec ./rest_server.exe
```

API available at `http://localhost:8080`

## API Usage

```powershell
# Generate bitonic sequence
curl.exe "http://localhost:8080/bitonic?n=5&l=1&r=10"
# Response: [1,2,3,4,5]

# Small sequence
curl.exe "http://localhost:8080/bitonic?n=3&l=5&r=7"
# [5,6,7]

# Invalid parameters
curl.exe "http://localhost:8080/bitonic?n=20&l=1&r=10"
# {"error": "Invalid parameters or impossible sequence"}
```

**Parameters:**
- `n` - Length of sequence
- `l` - Lower bound (inclusive)
- `r` - Upper bound (inclusive)

## Benchmark

```powershell
opam exec -- dune exec ./simple_bench.exe
```

**Results (10,000 elements):**
- Average time: ~56 μs per sequence
- Throughput: ~17,000 sequences/second

## Project Structure

```
bitonic-sequence/
├── bitonic.ml              # Core implementation
├── bitonic.mli             # Module interface
├── bitonic_test.ml         # Unit tests
├── simple_bench.ml         # Benchmarks
├── rest_server.ml          # REST API server
├── dune                    # Build configuration
├── docker-compose.yml      # Redis container
└── README.md               # This file
```