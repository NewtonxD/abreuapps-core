const CACHE_NAME = 'tile-cache';
const TILE_URL_PATTERN = "/API/tiles";

self.addEventListener('install', (event) => {
    event.waitUntil(
        caches.open(CACHE_NAME).then((cache) => {
        })
    );
});

self.addEventListener('fetch', (event) => {
    if (event.request.url.includes(TILE_URL_PATTERN)) {
        event.respondWith(
            caches.match(event.request).then((response) => {
                if (response) {
                    return response;
                }

                return fetch(event.request).then((networkResponse) => {
                    if (!networkResponse || networkResponse.status !== 200 || networkResponse.type !== 'basic') {
                        return networkResponse;
                    }

                    return caches.open(CACHE_NAME).then((cache) => {
                        cache.put(event.request, networkResponse.clone());
                        return networkResponse;
                    });
                });
            })
        );
    }
});
