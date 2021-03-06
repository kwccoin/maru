(ns maru.common.gtp.command.core (:require [maru.common.gtp.message.core :as message]))

(def maps
  '({:name "protocol_version" :count 0}
    {:name "name" :count 0}
    {:name "version" :count 0}
    {:name "known_command" :count 1}
    {:name "list_commands" :count 0}
    {:name "quit" :count 0}
    {:name "boardsize" :count 1}
    {:name "clear_board" :count 0}
    {:name "komi" :count 1}
    {:name "play" :count 2}
    {:name "genmove" :count 1}))

(def names (map #(:name %) maps))
(defn match [command-left command-right] (= command-left command-right))
(defn valid [command] (reduce #(or %1 (match command %2)) false maps))

(defn execute [name & [args]]
  (try (let [output (apply (ns-resolve 'maru.common.gtp.core (symbol name)) args)]
  (assoc output :message (message/response (str (:message output)))))
  (catch IllegalStateException e {:message message/error-unimplemented})
  (catch NullPointerException e {:message message/error-unimplemented})
  (catch IllegalArgumentException e {:message message/error-syntax})
  (catch Exception e {:message e})))
