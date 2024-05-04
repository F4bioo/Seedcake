package stub

import com.fappslab.features.common.domain.model.TransformationType
import com.fappslab.features.common.domain.usecase.DecryptParams
import com.fappslab.features.common.domain.usecase.EncryptParams
import com.fappslab.features.data.local.repository.SeedcakeRepositoryImplFixtures.ENCRYPTED
import com.fappslab.features.data.local.repository.SeedcakeRepositoryImplFixtures.PASSPHRASE
import com.fappslab.features.data.local.repository.SeedcakeRepositoryImplFixtures.SEED

internal val encryptParamsStub =
    EncryptParams(
        cipherSpec = TransformationType.GCM,
        readableSeedPhrase = SEED.split(" "),
        passphrase = PASSPHRASE,
    )

internal val decryptParamsStub =
    DecryptParams(
        unreadableSeedPhrase = ENCRYPTED,
        passphrase = PASSPHRASE
    )
