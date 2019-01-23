package hr.tvz.firechat.di.module

import android.content.Context
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import hr.tvz.firechat.data.interactor.*
import hr.tvz.firechat.data.repository.FirebaseMessagesRepository
import hr.tvz.firechat.data.repository.FirebaseUsersRepository
import hr.tvz.firechat.data.repository.MessagesRepository
import hr.tvz.firechat.data.repository.UsersRepository
import hr.tvz.firechat.ui.chat.ChatContract
import hr.tvz.firechat.ui.chat.ChatPresenter
import hr.tvz.firechat.ui.login.LoginContract
import hr.tvz.firechat.ui.login.LoginPresenter
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
    fun provideFirebaseUsersRepository(@Named(USERS_COLLECTION) ref: CollectionReference): UsersRepository =
        FirebaseUsersRepository(ref)

    @Provides
    @Singleton
    fun provideFirebaseMessagesRepository(@Named(MESSAGES_COLLECTION) ref: CollectionReference): MessagesRepository =
        FirebaseMessagesRepository(ref)

    /**
     * Interactor
     */
    @Provides
    fun provideFirebaseAuthUserInteractor(context: Context, authUI: AuthUI, firebaseAuth: FirebaseAuth): AuthUserInteractor =
        FirebaseAuthUserInteractor(context, authUI, firebaseAuth)

    @Provides
    fun provideFirebaseUserInteractor(repository: UsersRepository): UserInteractor =
        FirebaseUserInteractor(repository)

    @Provides
    fun provideFirebaseMessageInteractor(repository: MessagesRepository): MessagesInteractor =
        FirebaseMessagesInteractor(repository)

    /**
     * Presenter
     */
    @Provides
    fun provideLoginPresenter(authUserInteractor: AuthUserInteractor, usersRepository: UsersRepository): LoginContract.Presenter =
        LoginPresenter(authUserInteractor, usersRepository)

    @Provides
    fun provideChatPresenter(messagesInteractor: MessagesInteractor, authUserInteractor: AuthUserInteractor): ChatContract.Presenter =
        ChatPresenter(messagesInteractor, authUserInteractor)

    @Provides
    fun provideUserListPresenter(userInteractor: UserInteractor): UserListContract.Presenter =
        UserListPresenter(userInteractor)

    @Provides
    fun provideUserProfilePresenter(userInteractor: UserInteractor): UserProfileContract.Presenter =
            UserProfilePresenter(userInteractor)

    // MessageListPresenter
    // ProfilePresenter -> authInteractor, userInteractor?
}