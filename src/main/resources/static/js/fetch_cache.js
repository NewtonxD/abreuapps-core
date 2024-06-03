L.TileLayer.Fetch = L.TileLayer.extend({
    createTile: function(coords, done) {
        const tile = document.createElement('img');

        L.DomEvent.on(tile, 'load', L.Util.bind(this._tileOnLoad, this, done, tile));
        L.DomEvent.on(tile, 'error', L.Util.bind(this._tileOnError, this, done, tile));

        // Construct the tile URL
        const url = this.getTileUrl(coords);

        // Function to fetch tile and update cache
        const fetchTileAndCache = () => {
          fetch(url)
            .then(response => {
              if (!response.ok) {
                throw new Error('Network response was not ok');
              }
              return response.blob();
            })
            .then(blob => {
              const objectUrl = URL.createObjectURL(blob);
              tile.src = objectUrl;

              // Cache the response
              caches.open(TILE_CACHE_NAME).then(cache => {
                cache.put(url, new Response(blob));
              });

              // Cleanup the object URL after the image has loaded
              tile.onload = () => {
                URL.revokeObjectURL(objectUrl);
              };
            })
            .catch(error => {
              console.error('Fetch error:', error);
              done(error, null);
            });
        };

        // Check if the tile is already in the cache
        caches.open(TILE_CACHE_NAME).then(cache => {
          cache.match(url).then(response => {
            if (response) {
              response.blob().then(blob => {
                const objectUrl = URL.createObjectURL(blob);
                tile.src = objectUrl;

                // Cleanup the object URL after the image has loaded
                tile.onload = () => {
                  URL.revokeObjectURL(objectUrl);
                };
              });
            } else {
              fetchTileAndCache();
            }
          });
        });

        return tile;
    }
});

L.tileLayer.fetch = function(url) {
  return new L.TileLayer.Fetch(url);
};

