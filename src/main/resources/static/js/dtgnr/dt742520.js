$("#table").on("click","tbody tr",function(){

    let idDato=$(this).find('th').html();

    if(idDato===undefined || idDato===null)
        return;

    closeEventSource();

    let data={idDato:idDato};

    post_plantilla("/dtgnr/update",data);

});