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

(defn deploy-repository "Deploy the repository"
  [user repo wdir]
  (with-script-language :pallet.stevedore.bash/bash
    (script
     (deploy-git-clone-or-update-soft (str "git@github.com:" ~user "/" ~repo ".git") (str ~wdir "/" ~repo)))))

(fact
  (deploy-repository "ardumont" "my-org-files" "$HOME/repositories/perso")
  => "deploy-git-clone-or-update-soft git@github.com:ardumont/my-org-files.git $HOME/repositories/perso/my-org-files")

(defn deploy-repositories "Try to generate the deploy-repositories shell"
  [wdir]
  (let [repositories '("my-org-files" "reading-pro-git" "fest-assert-goodies"
                       "slash-my-home-slash-bin" "stumpwm-config" "my-mockito-lab"
                       "conkeror-saner-defaults" "my-project-euler-lab" "my-node-js-lab" 
                       "my-pallet-stevedore-lab" "luhnybin")
        repositories-denlab '("clojure-plays-checkers"
                              "clojure-dojo-at-lunch")]
    (reduce str ""
            (interpose "\n"
                       (concat
                        (map #(deploy-repository "ardumont" % wdir) repositories)
                        (map #(deploy-repository "denlab" % wdir) repositories-denlab))))))

;.;. Work is either fun or drudgery. It depends on your attitude. I like
;.;. fun. -- Barrett
(fact
  (deploy-repositories "$WDIR") => "deploy-git-clone-or-update-soft git@github.com:ardumont/my-org-files.git $WDIR/my-org-files\ndeploy-git-clone-or-update-soft git@github.com:ardumont/reading-pro-git.git $WDIR/reading-pro-git\ndeploy-git-clone-or-update-soft git@github.com:ardumont/fest-assert-goodies.git $WDIR/fest-assert-goodies\ndeploy-git-clone-or-update-soft git@github.com:ardumont/slash-my-home-slash-bin.git $WDIR/slash-my-home-slash-bin\ndeploy-git-clone-or-update-soft git@github.com:ardumont/stumpwm-config.git $WDIR/stumpwm-config\ndeploy-git-clone-or-update-soft git@github.com:ardumont/my-mockito-lab.git $WDIR/my-mockito-lab\ndeploy-git-clone-or-update-soft git@github.com:ardumont/conkeror-saner-default.git $WDIR/conkeror-saner-default\ndeploy-git-clone-or-update-soft git@github.com:ardumont/my-project-euler-lab.git $WDIR/my-project-euler-lab\ndeploy-git-clone-or-update-soft git@github.com:ardumont/my-node-js-lab.git $WDIR/my-node-js-lab\ndeploy-git-clone-or-update-soft git@github.com:ardumont/my-pallet-stevedore-lab.git $WDIR/my-pallet-stevedore-lab\ndeploy-git-clone-or-update-soft git@github.com:ardumont/luhnybin.git $WDIR/luhnybin\ndeploy-git-clone-or-update-soft git@github.com:denlab/clojure-plays-checkers.git $WDIR/clojure-plays-checkers\ndeploy-git-clone-or-update-soft git@github.com:denlab/clojure-dojo-at-lunch.git $WDIR/clojure-dojo-at-lunch")

(println "--------- END OF deploy-rep ----------" (java.util.Date.))
