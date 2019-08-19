package hr.tvz.firechat.di.module

import android.content.Context
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import hr.tvz.firechat.data.interactor.*
import hr.tvz.firechat.data.repository.FirebaseMessageRepository
import hr.tvz.firechat.data.repository.FirebaseUserRepository
import hr.tvz.firechat.data.repository.MessageRepository
import hr.tvz.firechat.data.repository.UserRepository
import hr.tvz.firechat.ui.chat.ChatContract
import hr.tvz.firechat.ui.chat.ChatPresenter
import hr.tvz.firechat.ui.login.LoginContract
import hr.tvz.firechat.ui.login.LoginPresenter
import hr.tvz.firechat.ui.main.MainContract
import hr.tvz.firechat.ui.main.MainPresenter
import hr.tvz.firechat.ui.profile.UserProfileContract
import hr.tvz.firechat.ui.profile.UserProfilePresenter
import hr.tvz.firechat.ui.userlist.UserListContract
import hr.tvz.firechat.ui.userlist.UserListPresenter
import hr.tvz.firechat.util.Constants.Firestore.MESSAGES_COLLECTION
import hr.tvz.firechat.util.Constants.Firestore.USERS_COLLECTION
import javax.inject.Named
import javax.inject.Singleton

@Module
class MainModule {

    /**
     * Firebase
     */
    @Provides
    @Singleton
    fun provideAuthUi(): AuthUI = AuthUI.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()


    @Named(USERS_COLLECTION)
    @Provides
    @Singleton
    fun provideFirestoreUsersRef(firestore: FirebaseFirestore): CollectionReference =
        firestore.collection(USERS_COLLECTION)

    @Named(MESSAGES_COLLECTION)
    @Provides
    @Singleton
    fun provideFirestoreMessagesRef(firestore: FirebaseFirestore): CollectionReference =
        firestore.collection(MESSAGES_COLLECTION)

    /**
     * Repository
     */
    @Provides
    @Singleton
    fun provideFirebaseUsersRepository(@Named(USERS_COLLECTION) ref: CollectionReference): UserRepository =
        FirebaseUserRepository(ref)

    @Provides
    @Singleton
    fun provideFirebaseMessagesRepository(@Named(MESSAGES_COLLECTION) ref: CollectionReference): MessageRepository =
        FirebaseMessageRepository(ref)

    /**
     * Interactor
     */
    @Provides
    fun provideFirebaseAuthUserInteractor(context: Context, authUI: AuthUI, firebaseAuth: FirebaseAuth): AuthUserInteractor =
        FirebaseAuthUserInteractor(context, authUI, firebaseAuth)

    @Provides
    fun provideFirebaseUserInteractor(repository: UserRepository): UserInteractor =
        FirebaseUserInteractor(repository)

    @Provides
    fun provideFirebaseMessageInteractor(repository: MessageRepository): MessagesInteractor =
        FirebaseMessagesInteractor(repository)

    /**
     * Presenter
     */
    @Provides
    fun provideLoginPresenter(authUserInteractor: AuthUserInteractor, userRepository: UserRepository): LoginContract.Presenter =
        LoginPresenter(authUserInteractor, userRepository)

    @Provides
    fun provideMainPresenter(authUserInteractor: AuthUserInteractor): MainContract.Presenter =
            MainPresenter(authUserInteractor)

    @Provides
    fun provideChatPresenter(messagesInteractor: MessagesInteractor, authUserInteractor: AuthUserInteractor, context: Context): ChatContract.Presenter =
        ChatPresenter(messagesInteractor, authUserInteractor, context)

    @Provides
    fun provideUserListPresenter(userInteractor: UserInteractor, authUserInteractor: AuthUserInteractor): UserListContract.Presenter =
        UserListPresenter(userInteractor, authUserInteractor)

    @Provides
    fun provideUserProfilePresenter(userInteractor: UserInteractor): UserProfileContract.Presenter =
            UserProfilePresenter(userInteractor)
}