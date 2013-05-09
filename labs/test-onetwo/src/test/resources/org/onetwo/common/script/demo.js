println("Start script \r\n");

// Output the type of an object
function printType(obj) {
    if (obj.getClass)
        println("    Java object: " + obj.getClass().name);
    else
        println("    JS object: " + obj.toSource());
    println("");
}

// Print variable
println("[JS] demoVar: " + demoVar);
printType(demoVar);

// Modify variable
demoVar = "value set in DemoScript.js";
println("[JS] demoVar: " + demoVar);
printType(demoVar);

// Set a new variable
var newVar = { x: 1, y: { u: 2, v: 3 } }
println("[JS] newVar: " + newVar);
printType(newVar);

println("End script \r\n");