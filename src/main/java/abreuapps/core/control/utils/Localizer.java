package abreuapps.core.control.utils;

import gg.jte.Content;

@FunctionalInterface
public interface Localizer {
    Content apply(String key);
}
