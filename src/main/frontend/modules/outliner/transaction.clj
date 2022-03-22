(ns frontend.modules.outliner.transaction)

(defmacro save-transactions
  "Currently there are no options.
  execute body and collect all transaction data generated by body,
  and call `listeners`
  (save-transactions {<option-map>}
    (insert-nodes ...)
    ;; do something
    (move-nodes ...)
    (delete-nodes ...))"
  [opts & body]
  (assert (map? opts))
  `(if (some? frontend.modules.outliner.core2/*transaction-data*)
     (do ~@body)
     (binding [frontend.modules.outliner.core2/*transaction-data* (transient [])]
       ~@body
       (let [~'r (persistent! frontend.modules.outliner.core2/*transaction-data*)]
         (when (seq ~'r)
           (doseq [~'listener @listeners]
             (~'listener ~'r)))))))
