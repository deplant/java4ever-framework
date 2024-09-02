package tech.deplant.java4ever.binding.gql;

public record ZerostateLibrariesFilter(StringFilter hash, StringFilter lib,
    StringArrayFilter publishers, ZerostateLibrariesFilter OR) {
}
