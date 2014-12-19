package org.onetwo.groovy

import org.onetwo.common.utils.FileUtils;


String basePath = "D:/mydev/workspace/onetwo/labs/test-onetwo/src/test/java/org/onetwo/groovy"
//this.getClass().getResource("").getPath()
println "path:$basePath"
File errorFile2 = new File("${basePath}/467-result-error-2.txt")

def datalist=[:]
errorFile2.splitEachLine("\\s") {
	def strs = it[1].split("\\[|\\]")
	datalist[strs[0][0..15]]=strs[1]
}

println "datalist: $datalist"

//用正确的身份证替换报盘文件里的姓名
File resultFile1 = new File("${basePath}/467-result-1.txt")
def index = 0
def newDatalist = []
resultFile1.splitEachLine("\t") {
	if(datalist.containsKey(it[3])){
		it[0] = datalist[it[3]]
		println "$index:$it"
		index++
		newDatalist.add(it)
	}
}

def BufferedWriter newWirter = new File("${basePath}/467-result-2.txt").newWriter("GBK")
try {
	newDatalist.each {
		println it
		newWirter.writeLine(it.join('\t'))
	}
} finally{
	newWirter.close()
}


