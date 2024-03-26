function constructForest(data) {
    var forest = $('<div class="forest"></div>');
    var rootNodes = findChildren(null,data);
    console.log(data);
    console.log(rootNodes);
    rootNodes.forEach(function(root) {
        var tree =  $('<div class="tree"></div>');
        var ul = $('<ul></ul>');
        tree.append(ul);
        constructBranch(ul, [root], data);
        forest.append($('<div class="row mt-4"></div>').append($('<div class="col-12"></div>').append(tree)));
    });
    
    var scriptEvent = $('<script>');
    scriptEvent.attr('src', '/content/js/lib/treeclickevent.js');
    forest.append(scriptEvent);


    return forest;
}

function constructBranch(parent, nodes, data) {
    nodes.forEach(function(node) {
        if (node[6]!=="Permisos"){
            var li = $('<li></li>');
            var input = $('<a><input type="checkbox" name="'+node[3]+'" '+(node[7]==="true"?'checked="checked"> ':'> ')+'</input>'+ node[5] + '</a>');
            li.append(input);

            var children = findChildren(node[3], data);
            if (children.length > 0) {
                var ul = $('<ul></ul>');
                li.append(ul);
                constructBranch(ul, children, data);
            }
            parent.append(li);
        }
    });
}

function findChildren(parent, data) {
    return data.filter(function(node) {
        console.log("node",node);
        return node[4] === parent && node[6]!=="Permiso";
    });
}

function constructSpecialAccess(data){
    var filter_data=filterDataSpecialAccess(data);
    var accessDiv=$("<div class='access'></div>");
    if(filter_data.length>0){
        filter_data.forEach(function(el){
            var specialAccess;
            if (el[2]==="Booleano"){
                specialAccess=$('<input type="checkbox" name="'+el[3]+'" '+(el[7]==="true"?'checked="checked"> ':'> ') + el[5] + '</input>');
            }else{
                specialAccess=$('<label for="'+el[3]+'">'+el[5]+' (Tipo '+el[2]+'): </label><input type="text" name="'+el[3]+'" id="'+el[3]+'" value="'+el[7]+'"/>');
            }
            accessDiv.append(specialAccess);
        });
    }
    return accessDiv;
}

function filterDataSpecialAccess(data){
    return data.filter(function(node) {
        return node[6]==="Permiso";
    });
}

function checkNone(){
    $('.tree input[type="checkbox"]').prop('checked',false);
}

function checkAll(){
    $('.tree input[type="checkbox"]').prop('checked',true);
}

$(function(){
    
    var data=window.data_acc;
    delete window.data_acc;
    
    var forest = constructForest(data);
    $(".container").prepend(forest);

    var specialAccess = constructSpecialAccess(data);
    $('.special-access').append(specialAccess);

});