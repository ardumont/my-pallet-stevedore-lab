(ns my-pallet-stevedore-lab.core
  (:use [clojure.test               :only [run-tests]])
  (:use [midje.sweet])
  (:use [clojure.contrib.repl-utils :only [show]])
  (:use [clojure.pprint             :only [pprint]])
  (:use [clojure.walk               :only [macroexpand-all]])
  (:use
   [pallet.common.string :only [quoted]]
   pallet.stevedore
   clojure.test)
  (:require
   [pallet.script :as script]
   [pallet.stevedore.common]
   [pallet.stevedore.bash]
   [pallet.common.filesystem :as filesystem]
   [pallet.common.logging.logutils :as logutils]
   [pallet.common.shell :as shell]
   [slingshot.core :as slingshot]
   [clojure.tools.logging :as logging]))

; try and learn how to play with stevedore
; https://github.com/pallet/stevedore/blob/develop/test/pallet/stevedore/bash_test.clj

(fact
  (with-script-language :pallet.stevedore.bash/bash
    (script (if (= foo bar) (println fred)))) => "if [ \\( \"foo\" == \"bar\" \\) ]; then echo fred;fi")

;.;. Happiness comes when you believe that you have done something truly
;.;. meaningful. -- Yan
(fact
  (with-script-language :pallet.stevedore.bash/bash (script (if (file-exists? "~/bin/cake") (println (quoted "cool"))))) => "if [ -e ~/bin/cake ]; then echo \"cool\";fi")
