/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */


$(function (){
    
    var table = new DataTable('#table', {
        language:{url:'/content/js/dataTables_es-ES.json'},
        lengthMenu: [10, 25, 50, { label: 'Todos', value: -1 }],
        pageLength:-1
    });
    
});