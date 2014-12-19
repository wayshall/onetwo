package org.onetwo.groovy

import org.onetwo.common.utils.FileUtils;


String basePath = "D:/mydev/workspace/onetwo/labs/test-onetwo/src/test/java/org/onetwo/groovy"
//this.getClass().getResource("").getPath()
println "path:$basePath"
File errorFile1 = new File("${basePath}/467-result-error-1.txt")

//截取卡号和身份证
def Map<String, String> datalist=[:]
errorFile1.splitEachLine("\\s") {
	def strs = it[1].split("\\[|\\]")
	datalist[strs[0][0..15]]=strs[1]
}

println "datalist: $datalist"

//用正确的身份证替换报盘文件里的身份证
File planFile = new File("${basePath}/467_5000.txt")
def index = 0
def newDatalist = []
planFile.splitEachLine("\t") {
	if(datalist.containsKey(it[3])){
		it[1] = datalist[it[3]]
		println "$index:$it"
		index++
		newDatalist.add(it)
	}
}

//写到文件
def resultFile1Path = "${basePath}/467-result-1.txt"
println "resultFile1Path: $resultFile1Path"
def BufferedWriter newWirter = new File(resultFile1Path).newWriter("GBK")
try {
	newDatalist.each {
		println it
		newWirter.writeLine(it.join('\t'))
	}
} finally{
	newWirter.close()
}


