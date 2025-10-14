let generate_bitonic n l r =
  if n <= 0 || l > r then None
  else if n = 1 then Some [l]
  else
    let range = r - l + 1 in
    if n > range then None
    else
      let k = (n + 1) / 2 in
      let inc = List.init k (fun i -> l + i) in
      let dec = List.init (n - k) (fun i -> l + k + (n - k - 1) - i) in
      Some (inc @ dec)
