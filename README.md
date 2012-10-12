# RefHeap, The Reference Heap!

This project is a lightweight Clojure pastebin that uses [Datomic](http://www.datomic.com/) and [Pygments](http://pygments.org) for syntax highlighting. The goal of this project to show how quickly one can use datomic with [datomic-simple](http://github.com/cldwalker/datomic-simple). For a before/after comparison in switching from mongo to datomic, [view the diff](https://github.com/cldwalker/datomic-refheap/compare/original...develop). This fork is feature complete with the [original](https://github.com/Raynes/refheap) except for session-store.

## Usage

Grab general project dependencies with lein, and grab Pygments with the provided bash script (requires [Mercurial](http://mercurial.selenic.com))

      # Until datomic-simple is a clojar or lein-git-deps works with this app
     sh-tty0$ git clone git://github.com/cldwalker/datomic-simple.git; cd datomic-simple; lein install; cd -

     sh-tty0$ lein deps
     sh-tty0$ ./bootstrap.sh

Start the RefHeap server using lein. The server will host content from [http://localhost:8080](http://localhost:8080).

     sh-tty1$ lein run

## Who We Are

We are [Anthony Grimes](https://github.com/Raynes) and [Alex McNamara](https://github.com/amcnamara). We like pastebins.

## License

Distributed under the Eclipse Public License, the same as Clojure; the terms of which can be found in the file epl-v10.html at the root of this disribution or at [http://opensource.org/licenses/eclipse-1.0.php](http://opensource.org/licenses/eclipse-1.0.php).
