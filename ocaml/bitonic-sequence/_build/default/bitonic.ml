let generate_bitonic n l r =
  if n <= 0 || l > r then None
  else
    let range = r - l + 1 in
    if n > range then None
    else
      let values = List.init n (fun i -> r - i) |> List.filter (fun x -> x >= l) in
      match values with
      | a :: b :: rest -> Some (b :: a :: rest)
      | _ -> Some values
