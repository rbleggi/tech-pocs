open OUnit2

let test_generate_bitonic _ =
  let open Bitonic in
  assert_equal (generate_bitonic 5 1 5) (Some [1;2;3;5;4]);
  assert_equal (generate_bitonic 1 10 20) (Some [10]);
  assert_equal (generate_bitonic 0 1 5) None;
  assert_equal (generate_bitonic 6 1 5) None;
  assert_equal (generate_bitonic 3 5 7) (Some [5;6;7]);
  assert_equal (generate_bitonic 4 2 5) (Some [2;3;5;4]);
  assert_equal (generate_bitonic 2 8 9) (Some [8;9]);
  assert_equal (generate_bitonic 2 9 8) None

let suite =
  "Bitonic tests" >::: [
    "test_generate_bitonic" >:: test_generate_bitonic
  ]

let () =
  run_test_tt_main suite
