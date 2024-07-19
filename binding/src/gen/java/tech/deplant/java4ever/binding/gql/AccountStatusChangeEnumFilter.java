package tech.deplant.java4ever.binding.gql;

import java.util.List;

public record AccountStatusChangeEnumFilter(AccountStatusChangeEnum eq, AccountStatusChangeEnum ne,
    AccountStatusChangeEnum gt, AccountStatusChangeEnum lt, AccountStatusChangeEnum ge,
    AccountStatusChangeEnum le, List<AccountStatusChangeEnum> in,
    List<AccountStatusChangeEnum> notIn) {
}
