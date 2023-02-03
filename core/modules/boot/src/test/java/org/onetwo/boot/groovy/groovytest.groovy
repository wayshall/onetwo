import org.onetwo.boot.groovy.TestRequest as TestRequest
def map = [page: 2, pageSize: 1000]
def req = map as TestRequest
println("req1: " + req)

map = [page: 12, page_size: 100]
def req2 = map as TestRequest
println("req2: " + req2)