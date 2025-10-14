let time_it f =
  let start = Unix.gettimeofday () in
  let result = f () in
  let finish = Unix.gettimeofday () in
  (finish -. start, result)

let () =
  let n = 10000 in
  let l = 1 in
  let r = 20000 in

  Printf.printf "==============================================\n";
  Printf.printf "Bitonic Sequence Generation Benchmark\n";
  Printf.printf "==============================================\n\n";
  Printf.printf "Parameters:\n";
  Printf.printf "  - Sequence length (n): %d\n" n;
  Printf.printf "  - Range: [%d, %d]\n" l r;
  Printf.printf "  - Iterations: 1000\n\n";

  Printf.printf "Running warm-up...\n";
  let _ = Bitonic.generate_bitonic n l r in

  Printf.printf "Running benchmark...\n";
  let iterations = 1000 in
  let total_time = ref 0.0 in

  for i = 1 to iterations do
    let (time, _) = time_it (fun () -> Bitonic.generate_bitonic n l r) in
    total_time := !total_time +. time;
    if i mod 100 = 0 then
      Printf.printf "  Progress: %d/%d iterations completed\r%!" i iterations
  done;

  Printf.printf "\n\n";
  Printf.printf "==============================================\n";
  Printf.printf "Results:\n";
  Printf.printf "==============================================\n";

  let avg_time = !total_time /. float_of_int iterations in
  let throughput = 1.0 /. avg_time in

  Printf.printf "Average time per sequence: %.2f μs\n" (avg_time *. 1_000_000.0);
  Printf.printf "Average time per sequence: %.6f ms\n" (avg_time *. 1_000.0);
  Printf.printf "Average time per sequence: %.9f s\n" avg_time;
  Printf.printf "\n";
  Printf.printf "Throughput: %.0f sequences/second\n" throughput;
  Printf.printf "Throughput: %.2f sequences/ms\n" (throughput /. 1_000.0);
  Printf.printf "\n";
  Printf.printf "Total execution time: %.3f seconds\n" !total_time;
  Printf.printf "==============================================\n"

