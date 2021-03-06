package com.sourcemuse.gradle.plugin

import spock.lang.Specification
import spock.lang.Unroll

class GradleMongoPluginExtensionSpec extends Specification {
    def pluginExtension = new GradleMongoPluginExtension()

    def 'port can be supplied as a number'() {
        given:
        pluginExtension.port = 12345

        when:
        def port = pluginExtension.port

        then:
        port == 12345
    }

    def 'port can be supplied as a String'() {
        given:
        pluginExtension.port = '12345'

        when:
        def port = pluginExtension.port

        then:
        port == 12345
    }

    @Unroll
    def 'port is randomized when supplied as #variant "#randomLabel"'() {
        given:
        pluginExtension.port = randomLabel

        when:
        def port = pluginExtension.port

        then:
        port >= 0 && port <= 65535

        where:
        variant      | randomLabel
        'lowercase'  | 'random'
        'uppercase'  | 'RANDOM'
        'mixed-case' | 'rAnDoM'
    }

    def 'repeated port checks are idempotent when a random port is picked'() {
        given:
        this.pluginExtension.port = 'random'

        when:
        def port = this.pluginExtension.port

        then:
        port == this.pluginExtension.port
    }

    @Unroll
    def 'mongod verbosity supplied #variant "#verbosityLabel" translates to -v'() {
        given:
        this.pluginExtension.mongodVerbosity = verbosityLabel

        when:
        def verbosity = this.pluginExtension.mongodVerbosity

        then:
        verbosity == '-v'

        where:
        variant                       | verbosityLabel
        'lowercase'                   | 'verbose'
        'uppercase'                   | 'VERBOSE'
        'mixed-case'                  | 'VerBose'
        'prefixed with hyphen'        | '-verbose'
        'prefixed with double hyphen' | '--verbose'
    }

    def 'mongod verbosity is automatically prefixed with a hyphen'() {
        given:
        this.pluginExtension.mongodVerbosity = 'v'

        when:
        def verbosity = this.pluginExtension.mongodVerbosity

        then:
        verbosity == '-v'
    }

    def "mongod verbosity can be supplied with multiple v's"() {
        given:
        this.pluginExtension.mongodVerbosity = 'vvvvv'

        when:
        def verbosity = this.pluginExtension.mongodVerbosity

        then:
        verbosity == '-vvvvv'
    }

    @Unroll
    def "mongod verbosity uppercase V's are turned into lowercase v's"() {
        given:
        this.pluginExtension.mongodVerbosity = suppliedVerbosity

        when:
        def verbosity = this.pluginExtension.mongodVerbosity

        then:
        verbosity == '-vvvvv'

        where:
        suppliedVerbosity << ['VVVVV', '-VVVVV']
    }

    def 'mongod verbosity containing any other characters is rejected'() {
        when:
        this.pluginExtension.mongodVerbosity = 'very verbose!'

        then:
        def throwable = thrown(IllegalArgumentException)
        throwable.getMessage() == "MongodVerbosity should be defined as either '-verbose' or '-v(vvvv)'. " +
                "Do not configure this property if you don't wish to have verbose output."
    }

    def 'mongod download url throws exception for invalid url'() {
        given:
        String invalidURL = 'thisisnotavalidurl'
        
        when:
        this.pluginExtension.downloadURL = invalidURL

        then:
        def throwable = thrown(IllegalArgumentException)
        throwable.message == "DownloadURL ${invalidURL} is not a valid URL."
    }

    def 'mongod download url can be set for valid url'() {
        given:
        String validURL = 'http://google.com'

        when:
        this.pluginExtension.downloadURL = validURL

        then:
        notThrown(IllegalArgumentException)
    }
}
