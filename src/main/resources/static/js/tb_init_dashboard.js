$(function (){
    
    var table = new DataTable('#table', {
        language:{url:'/content/js/dataTables_es-ES.json'},
        columnDefs: [{ targets: [ 2,3,4,5 ], className: 'dt-left'}] 
    });
    table.order([0,'desc']).draw();
});