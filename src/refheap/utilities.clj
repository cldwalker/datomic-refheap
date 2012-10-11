(ns refheap.utilities
  (:require [refheap.models.paste :as paste]))

(defn regenerate
  "Regenerates a paste's pygmentized text and preview text from
  its raw-contents. This is useful for when a lexer fails (because
  you typoed it in the language list) and you need to regenerate
  after you've fixed it."
  [id]
  (let [paste (paste/get-paste id)
        contents (:raw-contents paste)
        lexer (-> paste
                  :language
                  paste/lookup-lexer
                  second
                  :short)]
    (paste/update (:id paste)
      { :contents (:success (paste/pygmentize lexer contents))
        :summary (:success (paste/pygmentize lexer (paste/preview contents)))})))
