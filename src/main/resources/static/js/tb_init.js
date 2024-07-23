$(function (){
    
    var table = new DataTable('#table', {
        language:{url:'/content/js/dataTables_es-ES.json'}
    });
    table.order([0,'desc']).draw();
});