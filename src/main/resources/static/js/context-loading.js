
$(function(){
    
    $(".config-user").on("click",function(e){
        e.preventDefault;
        hideSidebar();
        
        if(typeof closeEventSource==='function') closeEventSource();
        
        get_plantilla("/usrmgr/myupdate");
        
    });
    
    $(".acceso").on("click",function(e){
        e.preventDefault;
        $(document).off("click","tbody tr");
        
        if(typeof closeEventSource==='function') closeEventSource();
        
        var id=$(this).attr("id");
        
        toggleSidebar();
        
        var data={id:id};
        post_plantilla("/main/content-page/",data);
        
    });
    
});
