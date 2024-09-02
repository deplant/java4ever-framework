package tech.deplant.java4ever.binding.gql;

import java.util.List;

public record AccessKey(String key, List<String> restrictToAccounts) {
}
