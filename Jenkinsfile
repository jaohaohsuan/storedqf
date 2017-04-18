properties(
    [
        [
            $class: 'jenkins.model.BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '5']
        ]
    ]
)
podTemplate(
    label: 'storedqf',
    containers: [
            containerTemplate(name: 'jnlp', image: 'henryrao/jnlp-slave', args: '${computer.jnlpmac} ${computer.name}', alwaysPullImage: true)
    ],
    volumes: [
            hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock'),
            persistentVolumeClaim(claimName: 'jenkins-ivy2', mountPath: '/home/jenkins/.ivy2', readOnly: false)
    ]) {

    node('storedqf') {
        ansiColor('xterm') {
            def esContaienr
            def esIPAddr = ''
            try {
                stage('prepare') {
                    checkout scm
                    esContaienr = docker.image('docker.elastic.co/elasticsearch/elasticsearch:5.3.0')
                                        .run('-e "xpack.security.enabled=false" -e "http.host=0.0.0.0" -e "transport.host=127.0.0.1"')
                    esIPAddr = containerIP(esContaienr)
                }

                docker.image('henryrao/sbt:2.11.8').inside("--net=container:${esContaienr.id}") {
                    stage('environment check') {
                        parallel elasticsearch: {
                            timeout(time: 30, unit: 'SECONDS') {
                                waitUntil {
                                    def r = sh script: "curl -XGET http://127.0.0.1:9200?pretty", returnStatus: true
                                    return (r == 0)
                                }
                            }
                        }, setup: {
                            build(job: 'inu-es-env/5.3',
                                        parameters: [
                                            string(name: 'ELASTICSEARCH_ADDR', value: "${esIPAddr}"),
                                            string(name: 'ELASTICSEARCH_PORT', value: '9200')
                                        ])
                        }, failFast: true
                    }
                    stage('build') {
                        sh 'du -sh ~/.ivy2'
                        sh 'sbt compile'
                    }
                    stage('unit test') {
                        sh 'sbt test'
                        step([$class: 'CucumberTestResultArchiver', testResults: 'target/cucumber/*.json'])
                    }
                }

            } catch (e) {
                echo "${e}"
                currentBuild.result = FAILURE
            }
            finally {
                esContaienr.stop()
                step([$class         : 'LogParserPublisher', failBuildOnError: true, unstableOnWarning: true, showGraphs: true,
                      projectRulePath: 'jenkins-rule-logparser', useProjectRule: true])
            }
        }
    }
}

def containerIP(container) {
    return sh(script: "docker inspect --format='{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' ${container.id}", returnStdout: true).trim()
}