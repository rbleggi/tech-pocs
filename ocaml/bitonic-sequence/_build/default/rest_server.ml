open Lwt.Syntax

(* Redis client setup *)
let redis_client () =
  let host =
    match Sys.getenv_opt "REDIS_HOST" with
    | Some h -> h
    | None -> "127.0.0.1"
  in
  let port =
    match Sys.getenv_opt "REDIS_PORT" with
    | Some p -> int_of_string p
    | None -> 6379
  in
  Redis_lwt.Client.connect { host; port }

let bitonic_handler request =
  let n = Dream.query request "n" in
  let l = Dream.query request "l" in
  let r = Dream.query request "r" in

  let parse_int_opt = function
    | Some s -> (try Some (int_of_string s) with _ -> None)
    | None -> None
  in

  match parse_int_opt n, parse_int_opt l, parse_int_opt r with
  | Some n_val, Some l_val, Some r_val ->
    let key = Printf.sprintf "%d_%d_%d" n_val l_val r_val in
    let* conn = redis_client () in
    let* cached = Redis_lwt.Client.get conn key in
    (match cached with
    | Some json_str ->
      Dream.json ~status:`OK json_str
    | None ->
      (match Bitonic.generate_bitonic n_val l_val r_val with
      | Some seq ->
        let json = `List (List.map (fun x -> `Int x) seq) in
        let json_str = Yojson.Safe.to_string json in
        let* _ = Redis_lwt.Client.set conn key json_str in
        Dream.json ~status:`OK json_str
      | None ->
        Dream.json ~status:`Bad_Request
          {|{"error": "Invalid parameters or impossible sequence"}|}))
  | _ ->
    Dream.json ~status:`Bad_Request
      {|{"error": "Missing or invalid parameters: n, l, r"}|}

let () =
  Dream.run
  @@ Dream.logger
  @@ Dream.router [
    Dream.get "/bitonic" bitonic_handler;
  ]
